JAVA ?= "/usr/bin/java"

ifneq (,$(wildcard .env))
    include .env
    export $(shell grep -v '^#' .env | xargs)
endif

all:
	@$(JAVA) --enable-preview ./solution.java
