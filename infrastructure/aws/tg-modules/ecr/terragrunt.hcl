terraform {
  source = "./ecr-module"
}

include "root" {
  path = find_in_parent_folders()
}


locals {
  common_vars = read_terragrunt_config("../../common.hcl")
}

inputs = {
  env = "${local.common_vars.locals.env}"
}