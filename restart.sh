#!/bin/bash
kubectl delete pods -n mvd --all
kubectl wait --namespace ingress-nginx \
  --for=condition=ready pod \
  --selector=app.kubernetes.io/component=controller \
  --timeout=90s

kubectl wait --namespace mvd --for=condition=ready pod --all --timeout=120s
./seed-k8s.sh
