terraform {
  source = "./acm-module"
}

include "root" {
  path = find_in_parent_folders()
}

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
  domain_name     = "${local.common_vars.locals.domain_name}"
  route53_zone_id = dependency.route53.outputs.route53_zone_id
}
