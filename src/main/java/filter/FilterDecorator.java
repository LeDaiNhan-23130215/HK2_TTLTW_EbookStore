package filter;

public abstract class FilterDecorator implements EbookQueryFilter {
    protected EbookQueryFilter wrapped;

    public FilterDecorator(EbookQueryFilter wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public QueryBase apply (QueryBase queryBase) {
        return wrapped.apply(queryBase);
    }
}
