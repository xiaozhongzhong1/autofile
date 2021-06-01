import pdfplumber
import sys


def extract(path, pageNo):
    with pdfplumber.open(path) as pdf:
        page01 = pdf.pages[pageNo]
        table2 = page01.extract_tables()
        return table2


def extract(path, start, end):
    with pdfplumber.open(path) as pdf:
        table = []
        for index in range(len(pdf.pages)):
            i = int(index)
            if i < start:
                continue
            if i > end:
                break
            pageindex = pdf.pages[i]
            if (pageindex):
                table.append(pageindex.extract_tables())

        return table


if __name__ == "__main__":
    path = sys.argv[1]
    start = sys.argv[2]
    end = sys.argv[3]
    # pageNo = sys.argv[2]
    # content = extract(path,int(pageNo))
    content = extract(path,int(start),int(end))
    print(content)
