# Define the name of the tar file
TARFILE = ex4.tar

# Define the files and directories to be included in the tar file
FILES = README pepse

# Default target
all: $(TARFILE)

# Rule to create the tar file
$(TARFILE): $(FILES)
	tar -cvf $(TARFILE) $(FILES)

# Clean target to remove the tar file
clean:
	rm -f $(TARFILE)

# Phony targets
.PHONY: all clean
