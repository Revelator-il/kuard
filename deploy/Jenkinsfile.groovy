def branch_env_map = [
        dev: 'dev',
        dev2: 'dev2',
        stage: 'stage',
]

//def nomad_api = [
//        dev: "https://dev-nomad-1.aws.revelator.tech:4646",
//        dev2: "https://dev2-nomad-1.aws.revelator.tech:4646",
//        stage: "https://stage-nomad-1.aws.revelator.tech:4646"
//]

pipeline {
    environment {
        APP_NAME = 'kuard'
        ECR_URI = '941538637259.dkr.ecr.us-west-2.amazonaws.com'
    }
    agent any
    options {
        buildDiscarder(logRotator(numToKeepStr: '2000'))
        skipDefaultCheckout()
        disableConcurrentBuilds()
        timestamps()
        ansiColor('xterm')
    }
    stages {
//        stage('Prepare') {
//            agent any
//            steps {
//                checkout scm
//                script {
//                    git_commit = sh(returnStdout: true, script: 'git rev-parse HEAD').trim()
//                    s_git_commit = git_commit.take(7)   // short git commit
//                    git_repo = sh(
//                            script: 'basename -s .git `git config --get remote.origin.url`',
//                            returnStdout: true
//                    ).trim()
//                    VERSION = s_git_commit
//                    IMAGE_NAME = "${APP_NAME}:commit_${VERSION}"
//                    currentBuild.displayName = "#${BUILD_NUMBER} - ${BRANCH_NAME}: ${VERSION}"
//                    DEPLOY_ENV = branch_env_map[BRANCH_NAME]
//                    DO_DEPLOY = is_auto_deploy(branch_env_map)
//                    DO_MIGRATIONS = 'yes'
//                    RUN_TESTS = 'yes'
//                    PRE_DEPLOY = 'yes'
//                }
//                prod_protect(DO_DEPLOY, DEPLOY_ENV)
//                withCredentials([string(credentialsId: 'jci_git_status', variable: 'access_token')]) {
//                    set_git_status([
//                            status: "pending",
//                            description: "JCI build status: pending",
//                            git_repo: "${git_repo}",
//                            git_commit: "${git_commit}",
//                            access_token: "${access_token}"
//                    ])
//                }
//            }
//        }
//        stage('Redefine params') {
//            agent any
//            when {
//                expression { is_replay() }
//            }
//            steps {
//                timeout(time: 300, unit: 'SECONDS') {
//                    script {
//                        def redef = input(
//                                message: 'Input required variables for manual triggered deploy',
//                                parameters: [
//                                        choice(
//                                                name: 'DEPLOY_ENV',
//                                                choices: "dev\nstage\nprod",
//                                                description: 'Deployment environment. dev/stage/prod...'
//                                        ),
//                                        choice(
//                                                name: 'RUN_TESTS',
//                                                choices: "yes\nno",
//                                                description: 'Perform deploy or not.'
//                                        ),
//                                        choice(
//                                                name: 'DO_DEPLOY',
//                                                choices: "no\nyes",
//                                                description: 'Perform deploy or not.'
//                                        ),
//                                        choice(
//                                                name: 'DO_MIGRATIONS',
//                                                choices: "yes\nno",
//                                                description: 'Perform deploy or not.'
//                                        ),
//                                        choice(
//                                                name: 'PRE_DEPLOY',
//                                                choices: "yes\nno",
//                                                description: 'Run pre-deploy stuff (migrations, etc.) or not.'
//                                        )
//                                ],
//                                submitterParameter: 'SUBMITTER',
//                                submitter: 'maksym@revelator.com,nikita@revelator.com,alain@revelator.com',
//                                ok: 'With great power comes great responsibility'
//                        )
//                        DEPLOY_ENV = redef['DEPLOY_ENV']
//                        DO_DEPLOY = redef['DO_DEPLOY']
//                        DO_MIGRATIONS = redef['DO_MIGRATIONS']
//                        RUN_TESTS = redef['RUN_TESTS']
//                        PRE_DEPLOY = redef['PRE_DEPLOY']
//                    }
//                }
//            }
//        }
//        stage('Build') {
//            agent any
//            steps {
//                echo 'Building..'
//                // checkout scm
//                timeout(time: 600, unit: 'SECONDS') {
//                    withCredentials([sshUserPrivateKey(credentialsId: 'github_back_utils', keyFileVariable: 'back_utils_key', passphraseVariable: '', usernameVariable: '')]) {
//                        sh "cp ${back_utils_key} back_utils_key"
//                        sh "docker build -t ${IMAGE_NAME} -f deploy/Dockerfile ."
//                    }
//                }
//            }
//        }
//        stage('Test') {
//            agent any
//            when {
//                anyOf {
//                    expression { return RUN_TESTS == 'yes'}
//                }
//            }
//            steps {
//                echo 'Testing..'
//                echo 'TODO: Implement!!!'
//                // checkout scm
//                // timeout(time: 600, unit: 'SECONDS') {
//                // withDockerContainer(
//                //     image: "${IMAGE_NAME}",
//                //     args: """
//                //         -m 1024m
//                //     """
//                // ){
//                //     sh 'pip install tox'
//                //     sh 'tox'
//                // }
//                // }
//            }
//        }
//        stage('Push image') {
//            agent any
//            steps {
//                echo 'Pushing..'
//                // checkout scm
//                timeout(time: 600, unit: 'SECONDS') {
//                    script{
//                        docker.withRegistry("https://${ECR_URI}") {
//                            image = docker.image("${IMAGE_NAME}")
//                            image.push()
//                        }
//                    }
//                }
//            }
//        }
//        stage('Pre-Deploy') {
//            agent {
//                docker {
//                    image 'demoontz/kubectl-aws2:5'
//                    args "-u 0"
//                }
//            }
//            when {
//                allOf {
//                    expression { return PRE_DEPLOY == 'yes'}
//                    expression { return DO_DEPLOY == 'yes'}
//                }
//            }
//            steps {
//
//                echo 'Deploying....'
//                script {
//                    env.DEBUG=true
//                    env.DEPLOY_ENV = DEPLOY_ENV
//                    env.IMAGE_NAME = IMAGE_NAME
//                    nomadfile = 'deploy/exec.hcl'
//                }
//                withAWS(credentials: 'dev-eks'){
//                    sh "aws eks --region us-west-2 update-kubeconfig --name dev-eks && kubectl get svc"
//                    sh "kubectl version"
//                    sh "kubectl cluster-info"
//                    sh "kubectl apply -f . deploy.yaml"
//                }            }
//        }
        stage('Deploy') {
            agent {
                docker {
                    image 'demoontz/kubectl-aws2:5'
                    args "-u 0"
                }
            }
//            when {
//                anyOf {
//                    expression { return DO_DEPLOY == 'yes'}
//                }
//            }
            steps {

                echo 'Deploying....'
//                script {
//                    env.DEBUG=true
//                    env.DEPLOY_ENV = DEPLOY_ENV
//                    env.IMAGE_NAME = IMAGE_NAME
//                }
                withAWS(credentials: 'dev-eks'){
                    sh "aws eks --region us-west-2 update-kubeconfig --name dev-eks && kubectl get svc"
                    sh "kubectl version"
                    sh "kubectl cluster-info"
 //                   sh "kubectl apply -f . deploy.yaml"
                }            }
        }
    }
    post {
        always {
            script {
                status_color = [
                        SUCCESS: "good",
                        UNSTABLE: "warning",
                        FAILURE: "danger",
                        ABORTED: "danger",
                ]
                github_status = [
                        SUCCESS: "success",
                        UNSTABLE: "failure",
                        FAILURE: "failure",
                        ABORTED: "failure",
                ]
            }
            slackSend(
                    color: "${status_color[currentBuild.currentResult]}",
                    channel: '#v2-ci',
                    message: "App: ${APP_NAME},\n Branch: ${BRANCH_NAME},\n Commit: ${VERSION},\n Result: ${currentBuild.currentResult},\n Details: ${BUILD_URL}",
                    teamDomain: 'revelator.com',
                    tokenCredentialId: 'jci_slack_status'
            )
            withCredentials([string(credentialsId: 'jci_git_status', variable: 'access_token')]) {
                set_git_status([
                        status: "${github_status[currentBuild.currentResult]}",
                        description: "JCI build status: ${currentBuild.currentResult}.toLowerCase()",
                        git_repo: "${git_repo}",
                        git_commit: "${git_commit}",
                        access_token: "${access_token}"
                ])
            }
        }
    }
}