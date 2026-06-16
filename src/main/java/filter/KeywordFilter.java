package filter;

public class KeywordFilter extends FilterDecorator{
    private String keyword;

    public KeywordFilter(EbookQueryFilter wrapped, String keyword) {
        super(wrapped);
        this.keyword = keyword;
    }

    @Override
    public QueryBase apply (QueryBase queryBase) {
        QueryBase q = super.apply(queryBase);

        if(keyword != null && !keyword.isEmpty()) {
            q.append(" AND LOWER(e.title) LIKE LOWER (?)", "%" + keyword + "%");
        }
        return q;
    }
}
