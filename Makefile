.PHONY: build push deploy local-up local-down

# Variables
IMAGE_NAME = movie-streaming
IMAGE_TAG = $(shell git rev-parse --short HEAD)
DOCKER_REPO = your-docker-repo

# Docker commands
build:
	docker build -t $(DOCKER_REPO)/$(IMAGE_NAME):$(IMAGE_TAG) .
	docker tag $(DOCKER_REPO)/$(IMAGE_NAME):$(IMAGE_TAG) $(DOCKER_REPO)/$(IMAGE_NAME):latest

push:
	docker push $(DOCKER_REPO)/$(IMAGE_NAME):$(IMAGE_TAG)
	docker push $(DOCKER_REPO)/$(IMAGE_NAME):latest

# Kubernetes commands
deploy:
	sed -i 's|__IMAGE__|$(DOCKER_REPO)/$(IMAGE_NAME):$(IMAGE_TAG)|g' k8s/deployment.yaml
	kubectl apply -f k8s/deployment.yaml
	kubectl apply -f k8s/service.yaml
	kubectl apply -f k8s/ingress.yaml

# Local development
local-up:
	docker-compose up -d

local-down:
	docker-compose down

# Clean up
clean:
	docker rmi $(DOCKER_REPO)/$(IMAGE_NAME):$(IMAGE_TAG) $(DOCKER_REPO)/$(IMAGE_NAME):latest