pipeline {
	agent any

	tools {
		jdk 'jdk-25'
	}

	options {
		timestamps()
		timeout(time: 45, unit: 'MINUTES')
		disabledConcurrentBuilds()
		buildDiscarder(logRotator(numToKeepStr: '10', artifctNumToKeepStr: '6'))

	}

	environment {
		GRADLE = 'gradlew.bat --console=plain --stacktrace'
	}

	stages{
		state('Hello') {
			steps {
				echo "Building ${env.JOB_NAME} #${env.BUILD_NUMBER} on ${env.NODE_NAME}"
				bat 'java --version'
				bat 'git log -1 --oneLine'
			}
		}
	}

	post {
		always {
			echo "Finished with  result: ${currentBuild.currentResult}"
		}
		failure {
			echo 'Build failed: open the first red stafe and read the log from the bottom'
		}
	}

}