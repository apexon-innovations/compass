terraform {
  source = "./route53-module"
}

include "root" {
  path = find_in_parent_folders()
}


locals {
  common_vars = read_terragrunt_config("../../common.hcl")
}

inputs = {
  domain_name = "${local.common_vars.locals.domain_name}"
}
