package filter;
public class FreeFilter extends FilterDecorator {
    private final Boolean isFree;

    public FreeFilter(EbookQueryFilter wrapped,
                      Boolean isFree) {
        super(wrapped);
        this.isFree = isFree;
    }

    @Override
    public QueryBase apply(QueryBase queryBase) {
        QueryBase q = super.apply(queryBase);
        if(isFree != null) {
            if(isFree) {
                q.append(" AND e.price = 0");
            } else {
                q.append(" AND e.price > 0");
            }
        }
        return q;
    }
}