module "acm_certificate" {
  source  = "terraform-aws-modules/acm/aws"
  version = "~> 4.3.2"

  domain_name        = var.domain_name
  zone_id            = var.route53_zone_id
  validation_timeout = "5m"

  subject_alternative_names = [
    "compass.${var.domain_name}",
  ]

}