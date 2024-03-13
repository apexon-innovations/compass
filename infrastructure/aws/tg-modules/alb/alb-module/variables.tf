variable "vpc_id" {
  description = "vpc id"
}

variable "environment" {
  description = "environment"
  default     = "prod"
}

variable "public_subnet_1" {
  description = "public subnets of the vpc"
}

variable "public_subnet_2" {
  description = "public subnets of the vpc"
}

variable "public_subnet_3" {
  description = "public subnets of the vpc"
}

# variable "acm_certificate_arn" {
# }

variable "route53_zone_id" {
}

variable "domain_name" {
}