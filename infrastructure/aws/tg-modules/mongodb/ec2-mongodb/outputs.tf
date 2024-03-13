output "instance_id" {
  value = aws_instance.example.id
}

output "mongo_url" {
  value = aws_instance.example.private_ip
}
