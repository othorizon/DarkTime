/**
 * 遍历执行元素，并且最后一个元素单独处理
 * @author Rizon
 * @date 2018/8/21
 */
public class ForeachAndLast<T> {
    private Collection<T> collection;

    public ForeachAndLast(Collection<T> collection) {
        this.collection = collection;
    }

    public ForeachAndLast<T> foreach(Consume<T> consume) {
        //不循环最后一个元素
        Iterator<T> iterator = collection.iterator();
        for (int i = 0; i < collection.size() - 1; i++) {
            consume.execute(iterator.next());
        }
        return this;
    }

    public void last(Consume<T> consume) {
        Iterator<T> iterator = collection.iterator();
        T last = null;
        while (iterator.hasNext()) {
            last = iterator.next();
        }
        consume.execute(last);
    }

    public interface Consume<T> {
        /**
         * 执行操作
         *
         * @param obj
         */
        void execute(T obj);
    }

    public static void main(String[] args) {
        List<String> a = new ArrayList<>();
        a.add("a");
        a.add("b");
        a.add("c");
        new ForeachAndLast<>(a)
                .foreach(element -> System.out.println("element:"+element))
                .last(last-> System.out.println("last:"+last));
    }

}