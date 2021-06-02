import sys

import pdfplumber

lastPage = 0
list = []


def checkIsLastPage(row, index):
    # 第一行的第一个字符是Key
    # 或者当前页和上一次出现表格的页相差 不止一页
    if row[0] == "Key" or (index - lastPage) != 1:
        return False
    else:
        return True


def handlePage(tables, index):
    if len(tables) == 0:
        return

    global list
    global lastPage

    # 当前页只有第一个表格有可能属于上一页
    for table in tables:
        firstRow = table[0]
        # 是否是正确的表格:一行有四列
        if len(firstRow) != 4:
            continue
        # 看是否是上一页的表格
        # 上一页的表格的话，则修改list的最后一个元素
        if checkIsLastPage(firstRow, index):
            pop = list.pop()
            # 第一行是否是上一行的延续？当第一行的第一列为空时，是上一页最后一行的延续
            nextPageFirstRow = table[0]

            if nextPageFirstRow[0] == "":
                lastPageLastRow = pop[len(pop) - 1]

                # 将下一页的第一行加上上一页的最后一行
                for index1 in range(len(lastPageLastRow)):
                    i = int(index1)
                    lastPageLastRow[i] = lastPageLastRow[i] + nextPageFirstRow[i]
                # 删除下一页的第一行
                table.pop(0)

            pop.extend(table)
            list.append(pop)
        else:
            list.append(table)

        lastPage = index


def extract(path, start, end):
    with pdfplumber.open(path) as pdf:

        for index in range(len(pdf.pages)):
            i = int(index)
            if i < start:
                continue
            if i > end:
                break
            # 处理当前页的表格
            handlePage(pdf.pages[i].extract_tables(), i)
        return list


if __name__ == "__main__":
    path = sys.argv[1]
    start = sys.argv[2]
    end = sys.argv[3]
    # pageNo = sys.argv[2]
    # content = extract(path,int(pageNo))
    content = extract(path, int(start), int(end))
    print(content)
    # fo = open("C:\\Users\\Administrator\\Desktop\\copy\\tables.txt", "w")
    # fo.write("".join(content))
