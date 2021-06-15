package com.unwulian.doc.pdf;

import com.unwulian.common.CatalogBean;
import com.unwulian.common.Chapter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.documentinterchange.logicalstructure.PDStructureElement;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageXYZDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class PDFCatalogParser {
    private PDDocument doc;
    private OutlineItemIterator outerIterator;
    private CatalogBean catalogBean = new CatalogBean();

    public PDFCatalogParser(PDDocument doc) {
        this.doc = doc;
        int total = doc.getNumberOfPages();
        PDDocumentInformation documentInformation = doc.getDocumentInformation();
        String title = documentInformation.getTitle();
        catalogBean.setTotalPages(total);
        catalogBean.setDocName(title);
        outerIterator = new OutlineItemIterator().iterator(doc);
    }


    /**
     * 获取目录信息
     *
     * @return
     */
    public CatalogBean getCatalogBean() throws IOException {
        //获取最外层的outlines
        List<PDOutlineItem> outerOutlines = new OutlineItemIterator().iterator(doc).getChildren();
        List<Chapter> outers = new ArrayList<>();
        for (PDOutlineItem outerOutline : outerOutlines) {
            Chapter recursive = recursive(outerOutline);
                        outers.add(recursive);
        }
        catalogBean.setChapters(outers);

        return catalogBean;
    }

    private Chapter getChapter(PDOutlineItem outerOutline) throws IOException {
        PDPageXYZDestination destination = (PDPageXYZDestination)outerOutline.getDestination();
        int page = destination.retrieveDestPageNumber();
        String title = outerOutline.getTitle();
        Chapter chapter = new Chapter();
        chapter.setPage(page);
        chapter.setTitle(title);
        chapter.setPdOutlineItem(outerOutline);
        chapter.setContent(doc);
        return chapter;
    }


    private Chapter recursive(PDOutlineItem item) throws IOException {
        Chapter chapter = getChapter(item);
        List<Chapter> childrenChapters = new ArrayList<>();
        List<PDOutlineItem> childrenItems = new OutlineItemIterator().iterator(item).getChildren();

        for (PDOutlineItem child : childrenItems) {
            Chapter recursive = recursive(child);
            childrenChapters.add(recursive);
        }
        chapter.setChildren(childrenChapters);
        return chapter;
    }



    private class OutlineItemIterator implements Iterator<PDOutlineItem> {
        private PDOutlineItem current;
        private PDOutlineItem next;

        private PDOutlineItem first;

        public OutlineItemIterator iterator(PDDocument doc) {
            this.first = doc.getDocumentCatalog().getDocumentOutline().getFirstChild();
            return this;
        }
        public OutlineItemIterator iterator(PDOutlineItem parent) {
            this.first = parent.getFirstChild();
            return this;
        }

        @Override
        public boolean hasNext() {
            if (current == null) {
                next = current = first;
            } else {
                next = current.getNextSibling();
                Optional.ofNullable(next).ifPresent(next -> current = next);
            }
            return next != null;
        }

        @Override
        public PDOutlineItem next() {
            return next;
        }

        /**
         * 循环获取所有对象
         *
         * @return
         */
        public List<PDOutlineItem> getChildren() {
            List<PDOutlineItem> list = new ArrayList<>();
            while (hasNext()) {
                PDOutlineItem next = next();
                list.add(next);
            }
            return list;
        }
    }

}
