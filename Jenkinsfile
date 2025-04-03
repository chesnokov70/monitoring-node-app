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
                            sparseCheckoutPaths: [[path: '.'], [path: 'terraform'], [path: 'ansible']]
                        ]
                    ], 
                    userRemoteConfigs: [
                        [
                            url: 'git@github.com:chesnokov70/monitoring-node-app.git',
                            credentialsId: 'ssh_github_access_key' // please use your jenkins access to git
                        ]
                    ]
                ])
            }
        }
        stage ('Terraform init') {
            steps {
                withCredentials([
                    string(credentialsId: 'aws-access-key-id', variable: 'AWS_ACCESS_KEY_ID'),
                    string(credentialsId: 'aws-secret-access-key', variable: 'AWS_SECRET_ACCESS_KEY')
                ]) {
                    sh '''
                    pwd && ls -lah
                    if [ ! -d "./terraform" ]; then echo "Terraform directory not found!"; exit 1; fi
                    cd ./terraform/
                    terraform init -reconfigure
                    '''
                }
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
                sh 'pwd && ls -lah'
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

    }
}
