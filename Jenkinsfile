def remote = [:]
def git_url = "git@github.com:chesnokov70/monitoring-node-app.git"
pipeline {
    agent any
    tools {
        terraform 'tf1.10.2'
    }
    options {
        ansiColor('xterm')
    }
    stages {
        stage('Clone Git repo Monitoring') {
            steps {
                checkout([
                    $class: 'GitSCM', 
                    branches: [[name: 'main']], 
                    doGenerateSubmoduleConfigurations: false, 
                    extensions: [
                        [
                            $class: 'SparseCheckoutPaths',
                            sparseCheckoutPaths: [[path: '.']]
                        ]
                    ], 
                    userRemoteConfigs: [
                        [
                            url: '$git_url',
                            credentialsId: 'ssh_github_access_key' // please use your jenkins access to git
                        ]
                    ]
                ])
            }
        }
        stage ('Terraform init') {
            steps {
                sh '''
                cd ./terraform/
                terraform init -reconfigure
                '''
            }
        }
        stage ('Terraform plan') {
            steps {
                sh '''
                cd ./terraform/
                terraform plan -out terraform.tfplan
                '''
            }
        }
        stage('Apply') {
            steps {
                sh '''
                cd ./terraform/
                terraform apply terraform.tfplan
                '''
            }
        }
        stage('Terraform output') {
            steps {
                sh '''
                cd ./terraform/
                terraform output web-address_monitoring > ../ansible/hosts
                '''
            }
        }
        stage('Install Ansible') {
            steps {
                sh '''
                sudo apt-add-repository --yes --update ppa:ansible/ansible
                sudo apt-get install ansible -y
                sudo pip install --upgrade urllib3 requests

                sleep 60
                '''
            }
        }
        stage('Run Ansible ') {
            steps {
                withCredentials([sshUserPrivateKey(credentialsId: 'ssh_instance_key', keyFileVariable: 'SSH_KEY')]) {
                    sh '''
                cd ./ansible/
                ansible-playbook -i hosts install_node_app.yml -u ubuntu --private-key='$SSH_KEY' -e 'ansible_ssh_common_args="-o StrictHostKeyChecking=no"'
                '''
                }
            }
        }
    }
}
