terraform {
  source = "./alb-module"
}

include "root" {
  path = find_in_parent_folders()
}

dependency "vpc" {
  config_path = "../vpc"
  mock_outputs = {
    vpc_id          = "vpc-05a4dcdb2152594d4"
    public_subnet_1 = "subnet-0f20bec0faadca5c2"
    public_subnet_2 = "subnet-0adb240ce4e56a562"
    public_subnet_3 = "subnet-0adb240ce4e56a562"
  }
}

// dependency "acm" {
//   config_path = "../acm"
//   mock_outputs = {
//     acm_certificate_arn = "arn:aws:acm:us-east-1:123456789012:certificate/1aadhsnc-1a0d-7823-b91c-09uch3d84524"
//   }
// }

dependency "route53" {
  config_path = "../route53"
  mock_outputs = {
    route53_zone_id = "abc123"
  }
}

locals {
  common_vars = read_terragrunt_config("../../common.hcl")
}

inputs = {
  vpc_id          = dependency.vpc.outputs.vpc_id
  public_subnet_1 = dependency.vpc.outputs.public_subnet_1
  public_subnet_2 = dependency.vpc.outputs.public_subnet_2
  public_subnet_3 = dependency.vpc.outputs.public_subnet_3
  env             = "${local.common_vars.locals.env}"
  #acm_certificate_arn = dependency.acm.outputs.acm_certificate_arn
  route53_zone_id = dependency.route53.outputs.route53_zone_id
  domain_name     = "${local.common_vars.locals.domain_name}"
}
  