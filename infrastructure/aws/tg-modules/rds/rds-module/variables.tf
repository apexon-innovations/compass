variable "environment" {
  default = "prod"
}

variable "vpc_id" {
  description = "vpc id"
}

variable "vpc_cidr_block" {
  description = "vpc cidr"
}

variable "private_subnets" {
  description = "private subnets"
}

variable "rds_master_username" {
  type        = string
  description = "The master username for the RDS instance"
  default     = "postgres"
}

variable "rds_master_password" {
  type        = string
  description = "The master password for the RDS instance"
  default     = ""
}

variable "rds_database_name" {
  type        = string
  description = "The database name for the RDS instance"
  default     = "compass"
}

variable "rds_port" {
  type        = number
  description = "The RDS instance port"
  default     = 5432
}

variable "statistic_period" {
  description = "statistcis period"
  default     = 60
}

variable "rds_cpu_usage_threshold" {
  description = "CPU thershold"
  default     = 80
}

variable "rds_local_storage_threshold" {
  description = "Stotage threshold"
  default     = 256 * 1000 * 1000 # 256 MB
}

variable "rds_freeable_memory_threshold" {
  description = "Memory thereshold"
  default     = 64
}

variable "notifications_email" {
  description = "email for alerts"
  default     = "support@example.com"
}

variable "db_subnet_group_name" {
  description = "database subnet group name"
}