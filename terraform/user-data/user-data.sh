#!/bin/bash

sudo yum install -y https://s3.amazonaws.com/ec2-downloads-windows/SSMAgent/latest/linux_amd64/amazon-ssm-agent.rpm
sudo docker plugin install  grafana/loki-docker-driver:latest --alias loki --grant-all-permissions

cat << EOF > /etc/docker/daemon.json
{
  "debug": true,
  "log-driver": "loki",
  "log-opts": {
    "loki-url": "http://loki.codemash-observability-workshop.com:3100/loki/api/v1/push",
    "loki-external-labels": "container_name={{.Name}},image_name={{.ImageName}}"
  }
}
EOF
service docker restart
echo "ECS_CLUSTER=${cluster}" >>/etc/ecs/ecs.config
echo "ECS_INSTANCE_ATTRIBUTES={\"workload\": \"${workload}\"}" >>/etc/ecs/ecs.config
