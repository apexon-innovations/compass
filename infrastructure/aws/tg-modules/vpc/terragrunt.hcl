terraform {
  source = "./vpc-module"
}

include "root" {
  path = find_in_parent_folders()
}


locals {
  common_vars = read_terragrunt_config("../../common.hcl")
}

inputs = {
  cluster_name = "compass-${local.common_vars.locals.env}-vpc"
  vpc_cidr     = "${local.common_vars.locals.vpc_cidr}"
  env          = "${local.common_vars.locals.env}"
}
