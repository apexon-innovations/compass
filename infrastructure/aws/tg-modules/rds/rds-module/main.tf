resource "random_password" "password" {
  count   = var.rds_master_password != "" ? 0 : 1
  length  = 20
  special = false
}

module "rds_cluster" {
  source = "terraform-aws-modules/rds-aurora/aws"

  name              = "compass-${var.environment}cluster"
  engine            = "aurora-postgresql"
  engine_mode       = "provisioned"
  engine_version    = "15.3"
  storage_encrypted = true

  port                        = var.rds_port
  database_name               = var.rds_database_name
  master_username             = var.rds_master_username
  master_password             = var.rds_master_password != "" ? var.rds_master_password : random_password.password[0].result
  manage_master_user_password = false

  vpc_id                 = var.vpc_id
  vpc_security_group_ids = [module.security_group_rds.security_group_id]
  db_subnet_group_name   = var.db_subnet_group_name
  create_security_group  = false

  apply_immediately            = true
  skip_final_snapshot          = false
  performance_insights_enabled = true
  deletion_protection          = true
  monitoring_interval          = 60

  serverlessv2_scaling_configuration = {
    min_capacity = 1
    max_capacity = 16
  }

  instance_class = "db.serverless"
  instances = {
    1 = {
      identifier = "compass-${var.environment}-instance-1"
    }
  }
}

module "security_group_rds" {
  source  = "terraform-aws-modules/security-group/aws"
  version = "~> 5.1"

  name            = "compass-${var.environment}-rds-sg"
  description     = "Control traffic to/from RDS instances"
  vpc_id          = var.vpc_id
  use_name_prefix = false

  # ingress
  ingress_with_cidr_blocks = [
    {
      from_port   = var.rds_port
      to_port     = var.rds_port
      protocol    = "tcp"
      description = "Allow inbound traffic from existing Security Groups"
      cidr_blocks = var.vpc_cidr_block
    }
  ]
}

resource "aws_sns_topic" "sns_topic_alerts" {
  name = "compass-${var.environment}-alerts"
}

resource "aws_sns_topic_subscription" "sns_topic_subscription_notifications" {
  topic_arn = aws_sns_topic.sns_topic_alerts.arn
  protocol  = "email"
  endpoint  = var.notifications_email
}

resource "aws_cloudwatch_metric_alarm" "cloudwatch_alarm_rds_cpu_usage" {
  alarm_name          = "compass-${var.environment}-rds-cpu-usage"
  comparison_operator = "GreaterThanThreshold"
  evaluation_periods  = 1
  metric_name         = "CPUUtilization"
  namespace           = "AWS/RDS"
  period              = var.statistic_period
  statistic           = "Average"
  threshold           = var.rds_cpu_usage_threshold
  alarm_description   = "Average database CPU utilization too high"

  alarm_actions = aws_sns_topic.sns_topic_alerts.*.arn
  ok_actions    = aws_sns_topic.sns_topic_alerts.*.arn

  dimensions = {
    DBClusterIdentifier = module.rds_cluster.cluster_id
  }
}

resource "aws_cloudwatch_metric_alarm" "cloudwatch_alarm_rds_local_storage" {
  alarm_name          = "compass-${var.environment}-rds-local-storage"
  comparison_operator = "LessThanThreshold"
  evaluation_periods  = 1
  metric_name         = "FreeLocalStorage"
  namespace           = "AWS/RDS"
  period              = var.statistic_period
  statistic           = "Average"
  threshold           = var.rds_local_storage_threshold
  alarm_description   = "Average database local storage too low"

  alarm_actions = aws_sns_topic.sns_topic_alerts.*.arn
  ok_actions    = aws_sns_topic.sns_topic_alerts.*.arn

  dimensions = {
    DBClusterIdentifier = module.rds_cluster.cluster_id
  }
}

resource "aws_cloudwatch_metric_alarm" "cloudwatch_alarm_rds_freeable_memory" {
  alarm_name          = "compass-${var.environment}-rds-freeable-memory"
  comparison_operator = "LessThanThreshold"
  evaluation_periods  = 1
  metric_name         = "FreeableMemory"
  namespace           = "AWS/RDS"
  period              = var.statistic_period
  statistic           = "Average"
  threshold           = var.rds_freeable_memory_threshold
  alarm_description   = "Average database random access memory too low"

  alarm_actions = aws_sns_topic.sns_topic_alerts.*.arn
  ok_actions    = aws_sns_topic.sns_topic_alerts.*.arn

  dimensions = {
    DBClusterIdentifier = module.rds_cluster.cluster_id
  }
}

resource "aws_cloudwatch_metric_alarm" "cloudwatch_alarm_rds_disk_queue_depth_high" {
  alarm_name          = "compass-${var.environment}-rds-disk-queue-depth-high"
  comparison_operator = "LessThanThreshold"
  evaluation_periods  = 1
  metric_name         = "DiskQueueDepth"
  namespace           = "AWS/RDS"
  period              = var.statistic_period
  statistic           = "Average"
  threshold           = var.rds_freeable_memory_threshold
  alarm_description   = "Average database disk queue depth too high"

  alarm_actions = aws_sns_topic.sns_topic_alerts.*.arn
  ok_actions    = aws_sns_topic.sns_topic_alerts.*.arn

  dimensions = {
    DBClusterIdentifier = module.rds_cluster.cluster_id
  }
}