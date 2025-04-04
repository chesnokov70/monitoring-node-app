def remote = [:]
def git_url = "git@github.com:chesnokov70/monitoring-node-app.git"
pipeline {
  agent any
  parameters {
    gitParameter (name: 'revision', type: 'PT_BRANCH')
  }
  environment {
    REGISTRY = "chesnokov70/monitoring-node-app"
    HOST = '54.224.143.232'
    SSH_KEY = credentials('ssh_instance_key')
    TOKEN = credentials('hub_token')
  }
  stages {
    stage('Configure credentials') {
      steps {
        withCredentials([sshUserPrivateKey(credentialsId: 'ssh_instance_key', keyFileVariable: 'private_key', usernameVariable: 'username')]) {
          script {
            remote.name = "${env.HOST}"
            remote.host = "${env.HOST}"
            remote.user = "$username"
            remote.identity = readFile("$private_key")
            remote.allowAnyHosts = true
          }
        }
      }
    }
    
    stage ('Clone repo') {
      steps {
        checkout([$class: 'GitSCM', branches: [[name: "${revision}"]], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'ssh_github_access_key', url: "$git_url"]]])
      }
    }

    stage ('Build and push') {
      steps {
        script {
         sh """ 
         scp /var/lib/jenkins/workspace/My_Lessons_Folder/monitoring-node-app/ansible/* root@${HOST}:/opt/ansible
         """
        }
      }
    }

    stage ('Deploy node-app') {
      steps {
        script {
          sshCommand remote: remote, command: """
          sudo chown jenkins:jenkins /home/ubuntu/.ssh/ssh_instance_key.pem
          chmod 600 /home/ubuntu/.ssh/ssh_instance_key.pem
          sudo apt update
          sudo apt install -y ansible
          ansible --version
          cd /opt/ansible
          ansible-playbook install_node_app.yml
          """
        }
      }
    }
  }    
} 