
oc new-app quay.io/quarkus/ubi-quarkus-native-s2i:19.3.1-java8~https://github.com/snandakumar87/self-healing-consumer
oc cancel-build bc/self-healing-consumer
oc patch bc/self-healing-consumer -p '{"spec":{"resources":{"limits":{"cpu":"4", "memory":"4Gi"}}}}'
oc start-build bc/self-healing-consumer
oc expose svc/self-healing-consumer
