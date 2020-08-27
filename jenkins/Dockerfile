FROM jenkins/jenkins:lts-alpine

ARG JAVA_OPTS
ENV JAVA_OPTS="-Djenkins.install.runSetupWizard=false ${JAVA_OPTS:-}"

COPY scripts/*.groovy /usr/share/jenkins/ref/init.groovy.d/
RUN mkdir -p /tmp/jenkins_shared && chown jenkins:jenkins /tmp/jenkins_shared

COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt
