module "alb" {
  source  = "terraform-aws-modules/alb/aws"
  version = "~> 8.7"

  name = "compass-public-${var.environment}-alb"

  load_balancer_type = "application"
  idle_timeout       = 3600

  vpc_id = var.vpc_id
  subnets = [
    "${var.public_subnet_1}",
    "${var.public_subnet_2}",
    "${var.public_subnet_3}",
  ]
  security_groups       = [module.security_group_alb.security_group_id]
  create_security_group = false

  http_tcp_listeners = [
    {
      port        = 80
      protocol    = "HTTP"
      action_type = "redirect"
      redirect = {
        port        = "443"
        protocol    = "HTTPS"
        status_code = "HTTP_301"
      }
    }
  ]

  # https_listeners = [
  #   {
  #     port            = 443
  #     protocol        = "HTTPS"
  #     certificate_arn = var.acm_certificate_arn
  #     action_type     = "redirect"
  #     redirect = {
  #       port        = "443"
  #       protocol    = "HTTPS"
  #       host        = "#{host}" # Redirect to chat subdomain
  #       status_code = "HTTP_301"
  #     }
  #   },
  # ]
}


module "security_group_alb" {
  source  = "terraform-aws-modules/security-group/aws"
  version = "~> 5.1"

  name            = "compass-${var.environment}-alb-sg"
  description     = "Allow all inbound traffic on the load balancer listener port"
  vpc_id          = var.vpc_id
  use_name_prefix = false

  ingress_cidr_blocks = ["0.0.0.0/0"]
  ingress_rules       = ["http-80-tcp", "https-443-tcp"]
  egress_rules        = ["all-all"]
}

resource "aws_route53_record" "route53_root_record" {
  zone_id = var.route53_zone_id
  name    = "compass.${var.domain_name}"
  type    = "A"

  alias {
    evaluate_target_health = false
    name                   = module.alb.lb_dns_name
    zone_id                = module.alb.lb_zone_id
  }
}


