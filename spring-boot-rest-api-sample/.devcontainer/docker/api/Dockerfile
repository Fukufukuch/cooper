FROM ubuntu:22.04

SHELL ["/bin/bash", "-c"]

RUN apt-get update \
    && apt-get install -y sudo unzip zip vim tar less iputils-ping curl jq git

ARG USER_NAME=vscode
ARG USER_GROUP_NAME=vscode
ARG USER_UID=1000
ARG USER_GID=1000

RUN groupadd --gid ${USER_GID} ${USER_GROUP_NAME} \
    && useradd --uid ${USER_UID} --gid ${USER_GID} --shell /bin/bash --create-home -m ${USER_NAME} \
    && echo "${USER_GROUP_NAME}	ALL=(ALL:ALL) NOPASSWD: ALL" >> /etc/sudoers.d/${USER_GROUP_NAME} \
    && chmod 0440 /etc/sudoers.d/${USER_GROUP_NAME}

ARG JAVA_VERSION=17.0.9-amzn
ARG GRADLE_VERSION=7.5

ARG SDKMAN_DIR=/home/vscode/.sdkman

USER vscode
RUN curl -s "https://get.sdkman.io" | bash \
    && source ${SDKMAN_DIR}/bin/sdkman-init.sh \
    && sdk install java ${JAVA_VERSION} \
    && sdk install gradle ${GRADLE_VERSION}
