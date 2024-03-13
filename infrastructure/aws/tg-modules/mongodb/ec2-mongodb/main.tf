data "aws_ami" "latest_amazon_linux" {
  most_recent = true
  owners      = ["amazon"]

  filter {
    name   = "name"
    values = ["amzn2-ami-hvm-*-x86_64-gp2"]
  }

  filter {
    name   = "virtualization-type"
    values = ["hvm"]
  }
}

resource "aws_iam_role" "ssm_role" {
  name = "mongo-SSMRole"

  assume_role_policy = <<-EOF
    {
      "Version": "2012-10-17",
      "Statement": [
        {
          "Effect": "Allow",
          "Principal": {
            "Service": "ec2.amazonaws.com"
          },
          "Action": "sts:AssumeRole"
        }
      ]
    }
  EOF
}

resource "aws_iam_role_policy_attachment" "attach_ssm_policy" {
  policy_arn = "arn:aws:iam::aws:policy/AmazonSSMFullAccess"
  role       = aws_iam_role.ssm_role.name
}

resource "aws_iam_instance_profile" "compass_instance_profile" {
  name = "ec2ssmprofile"
  role = aws_iam_role.ssm_role.name
}

resource "aws_instance" "example" {
  ami                         = data.aws_ami.latest_amazon_linux.id
  instance_type               = var.instance_type
  vpc_security_group_ids      = ["${aws_security_group.mongo_sg.id}"]
  key_name                    = null
  associate_public_ip_address = false
  subnet_id                   = var.private_subnet_id
  iam_instance_profile        = aws_iam_instance_profile.compass_instance_profile.name

  root_block_device {
    volume_type = "gp3"
    volume_size = 50
  }
  user_data = <<-EOF
              #!/bin/bash
              sudo yum update -y
              sudo yum install -y docker
              sudo service docker start
              sudo usermod -aG docker ec2-user
              sudo systemctl enable docker
              sudo mkdir /home/ec2-user/mongodb/data
              sudo chmod 777 /home/ec2-user/mongodb/data

              # Run MongoDB container
              docker run -d \
                --name mongodb \
                -p 27017:27017 \
                -v /home/ec2-user/mongodb/data:/data/db \
                --restart always \
                mongo
              EOF
  tags = {
    Name = "mongo-instance"
  }
}

resource "aws_security_group" "mongo_sg" {
  vpc_id = var.vpc_id

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = 27017
    to_port     = 27017
    protocol    = "tcp"
    cidr_blocks = [var.vpc_cidr]
  }
}


