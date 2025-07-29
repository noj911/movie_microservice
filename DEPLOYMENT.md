# Dockerization and Deployment Guide

This document provides a comprehensive guide to the dockerization and deployment approach for the Video Data Management Microservice.

## Table of Contents

1. [Overview](#overview)
2. [Docker Configuration](#docker-configuration)
3. [Local Development](#local-development)
4. [Kubernetes Deployment](#kubernetes-deployment)
5. [CI/CD Pipeline](#cicd-pipeline)
6. [Monitoring Setup](#monitoring-setup)
7. [Security Considerations](#security-considerations)
8. [Troubleshooting](#troubleshooting)

## Overview

The Video Data Management Microservice is part of a larger microservices architecture for a movie streaming platform. This service focuses on managing metadata for videos, while other services handle file storage, authentication, and transcoding.

The deployment strategy follows these principles:
- Containerization with Docker
- Orchestration with Kubernetes
- Continuous Integration and Deployment with GitHub Actions
- Monitoring with Prometheus and Grafana
- Security best practices throughout the pipeline

## Docker Configuration

### Dockerfile

The application uses a multi-stage build process:

1. **Build Stage**: Uses `maven:3.9.6-eclipse-temurin-21-alpine` to compile the application
   - Optimizes layer caching by separating dependency resolution from code compilation
   - Runs tests during the build process

2. **Runtime Stage**: Uses `eclipse-temurin:21-jre-alpine` for a lightweight runtime environment
   - Includes monitoring tools (curl for healthchecks)
   - Runs as a non-root user for security
   - Includes a healthcheck to verify application status
   - Sets appropriate memory limits

### Docker Compose

For local development, a `docker-compose.yml` file is provided that includes:
- The application container
- MongoDB database
- Prometheus for metrics collection
- Grafana for visualization

## Local Development

To run the application locally:

```bash
# Start the local development environment
make local-up

# Stop the local development environment
make local-down
```

This will start all the necessary services and expose:
- The application on http://localhost:8080
- MongoDB on localhost:27017
- Prometheus on http://localhost:9090
- Grafana on http://localhost:3000 (default credentials: admin/admin)

## Kubernetes Deployment

### Kubernetes Resources

The application is deployed to Kubernetes using the following resources:

1. **ConfigMap** (`k8s/configmap.yaml`): Contains configuration for connecting to other microservices
2. **Deployment** (`k8s/deployment.yaml`): Defines the application deployment with:
   - 3 replicas for high availability
   - Resource limits and requests
   - Health checks
   - Environment variables
3. **Service** (`k8s/service.yaml`): Exposes the application within the cluster
4. **Ingress** (`k8s/ingress.yaml`): Exposes the application outside the cluster with TLS

### Monitoring Resources

The monitoring stack is deployed using:

1. **Prometheus ConfigMap** (`k8s/prometheus-configmap.yaml`): Configuration for Prometheus
2. **Prometheus Deployment and Service** (`k8s/prometheus.yaml`): Deploys Prometheus
3. **Grafana Secret** (`k8s/grafana-secret.yaml`): Contains Grafana credentials
4. **Grafana Datasources** (`k8s/grafana-datasources.yaml`): Configures Prometheus as a data source
5. **Grafana Deployment and Service** (`k8s/grafana.yaml`): Deploys Grafana
6. **Monitoring Ingress** (`k8s/monitoring-ingress.yaml`): Exposes Prometheus and Grafana outside the cluster

### Manual Deployment

To deploy the application manually:

```bash
# Build the Docker image
make build

# Push the image to Docker Hub
make push

# Deploy the application to Kubernetes
make deploy

# Deploy the monitoring stack
make deploy-monitoring

# Deploy everything (application and monitoring)
make deploy-all
```

## CI/CD Pipeline

The application uses GitHub Actions for CI/CD, defined in `.github/workflows/ci-cd.yml`:

### Pipeline Stages

1. **Build and Test**:
   - Checks out the code
   - Sets up JDK 21
   - Builds the application with Maven
   - Runs tests
   - Uploads the built JAR as an artifact

2. **Build and Push Docker Image**:
   - Builds a Docker image using the Dockerfile
   - Tags the image with the commit SHA and branch name
   - Pushes the image to Docker Hub
   - Uses caching to speed up builds

3. **Deploy to Kubernetes**:
   - Deploys the application to a Kubernetes cluster
   - Updates the deployment.yaml file with the new image tag
   - Applies the Kubernetes manifests
   - Deploys the monitoring stack
   - Verifies the deployment

### Required Secrets

The following secrets need to be configured in your GitHub repository:
- `DOCKER_REPO`: Your Docker Hub repository name
- `DOCKERHUB_USERNAME`: Your Docker Hub username
- `DOCKERHUB_TOKEN`: Your Docker Hub access token
- `KUBE_CONFIG`: Your Kubernetes cluster configuration

## Monitoring Setup

The application is monitored using Prometheus and Grafana:

### Prometheus

- Scrapes metrics from the application's `/actuator/prometheus` endpoint
- Uses Kubernetes service discovery to find pods with the appropriate annotations
- Stores metrics for querying and alerting

### Grafana

- Visualizes metrics from Prometheus
- Provides dashboards for monitoring the application
- Can be extended with alerts

### Accessing Monitoring

- Prometheus: https://prometheus.example.com
- Grafana: https://grafana.example.com (default credentials: admin/admin)

## Security Considerations

The deployment includes several security best practices:

1. **Container Security**:
   - Running as a non-root user
   - Using minimal base images
   - Regular updates of dependencies

2. **Kubernetes Security**:
   - Secrets for sensitive information
   - Resource limits to prevent DoS
   - Network policies (to be implemented)

3. **TLS Encryption**:
   - HTTPS for all external access
   - Let's Encrypt for certificate management

4. **Authentication and Authorization**:
   - Integration with the Authentication Service
   - Service-to-service authentication

## Troubleshooting

### Common Issues

1. **Application not starting**:
   - Check logs: `kubectl logs deployment/movie-streaming`
   - Verify MongoDB connection: `kubectl get secret mongodb-credentials -o yaml`

2. **Monitoring not working**:
   - Check Prometheus logs: `kubectl logs deployment/prometheus`
   - Verify Prometheus configuration: `kubectl get configmap prometheus-config -o yaml`
   - Check if metrics endpoint is accessible: `kubectl port-forward deployment/movie-streaming 8080:8080` and then access http://localhost:8080/actuator/prometheus

3. **CI/CD pipeline failures**:
   - Check GitHub Actions logs
   - Verify secrets are correctly configured
   - Ensure Kubernetes cluster is accessible