# Automatically find all directories starting with "day"
DAYS := $(wildcard day*)
MAKEFLAGS += --silent

# Default target: Build all days
.PHONY: all
all: $(DAYS)

# Target for each day
.PHONY: $(DAYS)
$(DAYS):
	@echo "Solution(s) for $@:"
	$(MAKE) -C $@
	@echo ""

