import os
import re
import sys
import logging

def check_coding_style(file_path):
    """
    Checks a Java source file against the coding style guidelines.
    """
    with open(file_path, 'r') as file:
        content = file.read()

    # 1. Naming Style Summary
    logging.info(f"Checking Naming Style for {file_path}:")
    # 1.1. Class names
    class_names = re.findall(r'class\s+(\w+)', content)
    for name in class_names:
        if not name.istitle():
            logging.warning(f"{file_path}: Class name '{name}' does not follow the convention (first letter capitalized).")
    # 1.2. Variable and data member names
    variable_names = re.findall(r'\b(\w+)\b\s*=', content)
    for name in variable_names:
        if name[0].isupper():
            logging.warning(f"{file_path}: Variable name '{name}' should start with a lowercase letter.")
    # 1.3. Method names
    method_names = re.findall(r'(\w+)\(', content)
    constructor_names = [name for name in method_names if name in class_names]
    for name in method_names:
        if name[0].isupper() and name not in constructor_names:
            logging.warning(f"{file_path}: Method name '{name}' should start with a lowercase letter.")
    # 1.4. Constants
    constant_names = re.findall(r'final\s+\w+\s+(\w+)\s*=', content)
    for name in constant_names:
        if not name.isupper() or '_' not in name:
            logging.warning(f"{file_path}: Constant name '{name}' does not follow the convention (all uppercase with '_' as word separator).")

    # 2. Indentation
    logging.info(f"Checking Indentation for {file_path}:")
    lines = content.splitlines()
    for i, line in enumerate(lines):
        if line.startswith(' '):
            logging.warning(f"{file_path}: Line {i+1} uses spaces for indentation instead of tabs.")
        if line.strip().startswith('}') and line.index('}') != 0:
            logging.warning(f"{file_path}: Line {i+1} closing bracket is not aligned with the opening bracket.")

    # 3. Method Design
    logging.info(f"Checking Method Design for {file_path}:")
    methods = [m for m in method_names if m not in constructor_names]
    for method in methods:
        lines_in_method = len(re.findall(r'\n\s*\w+', content[content.index(f"{method}("):]))
        if lines_in_method > 35:
            logging.warning(f"{file_path}: Method '{method}' has more than 35 lines and should be considered for refactoring.")
        num_params = len(re.findall(r'\w+\s+\w+', re.search(rf'{method}\(([^)]*)\)', content).group(1)))
        if num_params > 4:
            logging.warning(f"{file_path}: Method '{method}' has more than 4 parameters and should be considered for refactoring.")

    # 4. Comments
    logging.info(f"Checking Comments for {file_path}:")
    comment_blocks = re.findall(r'/\*.*?\*/', content, re.DOTALL)
    for block in comment_blocks:
        if '\n' not in block:
            logging.warning(f"{file_path}: Single-line comment block '{block}' should be a single-line comment.")
        if block.count('\n') > 2 and '/**' not in block:
            logging.warning(f"{file_path}: Multi-line comment block '{block}' should be a Javadoc comment.")

    logging.info(f"Done checking coding style for {file_path}.")

if __name__ == "__main__":
    logging.basicConfig(level=logging.INFO, format='%(levelname)s: %(message)s')

    if len(sys.argv) < 2:
        logging.error("Usage: python script.py <file1.java> [file2.java] [file3.java] ...")
        sys.exit(1)

    for file_path in sys.argv[1:]:
        if os.path.isfile(file_path) and file_path.endswith(".java"):
            check_coding_style(file_path)
        else:
            logging.warning(f"Skipping {file_path} (not a valid Java file).")