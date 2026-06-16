package filter;

import java.util.List;

public class FormatFilter extends FilterDecorator {

    private final List<String> formats;

    public FormatFilter(EbookQueryFilter wrapped, List<String> formats) {
        super(wrapped);
        this.formats = formats;
    }

    @Override
    public QueryBase apply(QueryBase queryBase) {
        QueryBase q = super.apply(queryBase);
        if(formats != null && !formats.isEmpty()) {
            StringBuilder bs = new StringBuilder();
            for(String format : formats) {
                bs.append("?, ");
            }
            String placeholders = !bs.isEmpty() ? bs.substring(0, bs.length() - 2) : "";
            q.append(" AND f.fileFormat IN (" + placeholders + ")", formats.toArray()
            );
        }
        return q;
    }
}
