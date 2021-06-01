

import pdfplumber
import sys

def extract(path,pageNo):
    with pdfplumber.open(path) as pdf:
        page01 = pdf.pages[pageNo]
        table2 = page01.extract_tables()
        return table2

if __name__ == "__main__":
    path = sys.argv[1]
    pageNo = sys.argv[2]
    content = extract(path,int(pageNo))
    print(content)
