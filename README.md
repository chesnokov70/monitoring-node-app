# monitoring-node-app
# Lessons-nastya
#----------------
# С помощью terraform создать машину для мониторинга
# С помощью ansible установить docker
# С помощью ansible и docker запустить на машине мониторинга prometheus + grafana # + loki + alertmanager. Проверить, что все компоненты работоспособны
# 
# Access Services
# 
# chmod 644 loki-config.yaml
#
# Prometheus → http://<server-ip>:9090
# Grafana → http://<server-ip>:3001 (Login: admin / admin)
# Alertmanager → http://<server-ip>:9093
# Loki → http://<server-ip>:3100
# node-app → http://<server-ip>:80
# node-app → http://<server-ip>:3000
# cAdvisor → http://<server-ip>:8080
# Loki → http://<server-ip>:3100

#
# sudo /bin/systemctl daemon-reload
# sudo /bin/systemctl enable grafana-server

#
# С помощью terraform создать машину для jenkins
# С помощью ansible установить jenkins
# Поднять свой secure docker registry
#
# -------------------------------------------
# aws ec2 describe-subnets --query "Subnets[*].{ID:SubnetId,CIDR:CidrBlock}" --output table
#
# terraform fmt
# terraform validate
# terraform apply -auto-approve
#
# terraform fmt
# terraform validate
# terraform apply -auto-approve
