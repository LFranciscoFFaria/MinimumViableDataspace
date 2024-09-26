#!/bin/bash
# Create the cluster
kind create cluster -n dcp-demo --config deployment/kind.config.yaml

# Load docker images into KinD
kind load docker-image controlplane:latest dataplane:latest identity-hub:latest catalog-server:latest -n dcp-demo

# Deploy an NGINX ingress
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/main/deploy/static/provider/kind/deploy.yaml

# Wait for the ingress controller to become available
kubectl wait --namespace ingress-nginx \
  --for=condition=ready pod \
  --selector=app.kubernetes.io/component=controller \
  --timeout=90s

# Deploy the dataspace, type 'yes' when prompted
cd deployment
terraform init
terraform apply
