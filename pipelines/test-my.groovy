pipeline {
    agent any 
    parameters {
        choice(name: 'playbook', choices: ["ansible/install_node_app.yml"])
        string(name: 'host', defaultValue: '54.87.119.152', trim: true)
        string(name: 'branch', defaultValue: 'main', description: 'Git branch to checkout') // Replaced 'gitParameter' with 'string'
        booleanParam(name: 'dryrun', defaultValue: true)
    }
    environment {
        GIT_URL = 'git@github.com:chesnokov70/monitoring-node-app.git'  // Defined missing variable
    }
    stages {
        stage('Checkout repo') {
            steps {
                checkout([
                    $class: 'GitSCM', 
                    branches: [[name: "*/${params.branch}"]], 
                    doGenerateSubmoduleConfigurations: false, 
                    extensions: [], 
                    submoduleCfg: [], 
                    userRemoteConfigs: [[
                        credentialsId: 'ssh_github_access_key', 
                        url: "${env.GIT_URL}"
                    ]]
                ])
            }
        }
      
        stage('DryRun') {
            when {
                expression { params.dryrun }
            }
            steps {
                sh """
                  export ANSIBLE_HOST_KEY_CHECKING=False
                  ansible-playbook -i \"${params.host},\" ${params.playbook} --check
                """
            }
        }

        stage('Apply') {
            when {
                expression { !params.dryrun }
            }
            steps {
                sh """
                  export ANSIBLE_HOST_KEY_CHECKING=False
                  ansible-playbook -i \"${params.host},\" ${params.playbook}
                """
            }
        }
        stage('Cleanup Workspace') {
            steps {
                cleanWs()
            }
        }

    }
}
