FROM centos:centos8

ARG VERSION="3.22"
ARG user=jenkins
ARG group=jenkins
ARG uid=1000
ARG gid=1000
ARG AGENT_WORKDIR=/home/${user}

RUN groupadd -g ${gid} ${group}
RUN useradd -c "Jenkins user" -d /home/${user} -u ${uid} -g ${gid} -m ${user}
LABEL Description="This is a base image, which provides the Jenkins swarm agent client executable" Vendor="Jenkins swarm client" Version="${VERSION}"

RUN yum -y update &&\
    yum -y install epel-release git java-1.8.0-openjdk.x86_64 wget autoconf make automake libtool perl openssl-devel python2 python2-pip

USER ${user}

WORKDIR ${AGENT_WORKDIR}
RUN wget -q https://repo.jenkins-ci.org/releases/org/jenkins-ci/plugins/swarm-client/3.22/swarm-client-3.22.jar
COPY "run.sh" /usr/bin
 
CMD ["run.sh"]
