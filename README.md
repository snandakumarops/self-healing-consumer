# Self Healing infrastructure consumer - UI
The consumer is a sample UI application, which provides a way to visualize the exchanges between the various topics across the 4 micro services.
```
oc new-app registry.access.redhat.com/ubi8/openjdk-11:latest~https://github.com/snandakumar87/self-healing-consumer
oc expose svc/self-healing-consumer
```