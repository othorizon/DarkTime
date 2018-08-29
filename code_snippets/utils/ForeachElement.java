/**
 * 遍历执行元素，并且最后一个元素单独处理
 *
 * @author Rizon
 * @date 2018/8/21
 */
public class ForeachElement<T> {
    private List<T> elements;
    private boolean first = false;
    private boolean last = false;

    public ForeachElement(@NonNull Iterable<? extends T> elements) {
        this.elements = Lists.newArrayList(elements);
    }

    public ForeachElement<T> first(Consume<T> consume) {
        first = true;
        if (!elements.isEmpty()) {
            consume.execute(elements.get(0));
        }
        return this;
    }

    public ForeachElement<T> last(Consume<T> consume) {
        last = true;
        if (!elements.isEmpty()) {
            consume.execute(elements.get(elements.size() - 1));
        }
        return this;
    }

    /**
     * 遍历剩余的元素
     *
     * @param consume
     * @return
     */
    public ForeachElement<T> others(Consume<T> consume) {
        for (int i = 0; i < elements.size(); i++) {
            if (i == 0 && first) {
                continue;
            }
            if (i == elements.size() - 1 && last) {
                continue;
            }
            consume.execute(elements.get(i));
        }
        return this;
    }


    public interface Consume<T> {
        /**
         * 执行操作
         *
         * @param obj
         */
        void execute(T obj);
    }

}