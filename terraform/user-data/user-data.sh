#!/bin/bash

sudo yum install -y https://s3.amazonaws.com/ec2-downloads-windows/SSMAgent/latest/linux_amd64/amazon-ssm-agent.rpm
echo "ECS_CLUSTER=${cluster}" >>/etc/ecs/ecs.config
echo "ECS_INSTANCE_ATTRIBUTES={\"workload\": \"${workload}\"}" >>/etc/ecs/ecs.config
