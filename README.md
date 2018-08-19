# The zcp-alertmanager Installation Guide

> zcp-alertmanager 는 zcp-portal-ui (Console)의 back-end api server 로서, Prometheus와 alertmanager의 Proxy 역할을 하는 API Server 이다.
> zcp-alertmanager 을 설치하기 이전에 k8s cluster 가 설치되어 있어야 하고, cluster role 권한으로 `kubectl` 을 수행 할 수 있는 환경을 갖추어야 한다.
> zcp-alertmanager-store 의 데이터를 사용하므로 설치가 미리 되어 있어야 한다.

## Clone this project into the desktop
```
$ git clone https://github.com/cnpst/zcp-alertmanager.git
```

## Deploy the application

k8s configuration 파일 디렉토리로 이동한다.

```
$ cd zcp-alertmanager/k8s
```

### :one: ConfigMap을 생성 한다.
#### ConfigMap 생성
```
$ kubectl create -f zcp-alertmanager-config.yaml
```
### :two: Secret을 수정한 후 생성 한다.
(:white_check_mark: zcp-alertmanager-store 설치 시 MariaDB의 admin id/password 변경하지 않은 경우 그대로 사용하면 됨)

#### Secret 생성

```
$ kubectl create -f zcp-alertmanager-secret.yaml
```

### :four: Deployment와 Service를 생성 한다.
zcp-alertmanager 의 container image tag 정보를 확인 한 후, 생성 한다.

현재는 bluemix container registry `image: registry.au-syd.bluemix.net/cloudzcp/zcp-alertmanager:0.9.3` 를 사용한다.

```
$ kubectl create -f zcp-alertmanager-deployment-ibm.yaml
```

다음 명령어로 zcp-iam 이 정상적으로 배포되었는지 확인한다.
```
$ kubectl get pod -n zcp-system
```

