variable "cluster_name" {
  description = "cluster name"
}

variable "cluster_version" {
  description = "cluster_version"
  default     = 1.29
}

variable "ami_release_version" {
  description = "ami_release_version"
}

variable "vpc_id" {
  description = "vpc id for the cluster"
}

variable "private_subnet_1" {
  description = "private subnets of the vpc"
}

variable "private_subnet_2" {
  description = "private subnets of the vpc"
}

variable "private_subnet_3" {
  description = "private subnets of the vpc"
}