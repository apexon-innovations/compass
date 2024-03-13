output "vpc_id" {
  value = module.vpc.vpc_id
}
output "private_subnet_1" {
  value = module.vpc.private_subnets[0]
}

output "private_subnet_2" {
  value = module.vpc.private_subnets[1]
}

output "private_subnet_3" {
  value = module.vpc.private_subnets[2]
}
output "private_subnets" {
  value = module.vpc.private_subnets
}
output "public_subnet_1" {
  value = module.vpc.public_subnets[0]
}

output "public_subnet_2" {
  value = module.vpc.public_subnets[1]
}
output "public_subnet_3" {
  value = module.vpc.public_subnets[2]
}

output "database_subnet_group_name" {
  value = module.vpc.database_subnet_group_name
}