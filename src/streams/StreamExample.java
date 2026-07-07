package streams;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamExample {

    public static void main(String[] args) {
        List<Integer> nums = Arrays.asList(1,2,3,4,5,6,7,8,9);

        List<String> names = Arrays.asList("mohit", "gpt", "Claude");

        // Streams can only be consumed once
        Stream<Integer> data = nums.stream();
        // calling any method on data will consume it
        //long count = data.count(); // this will give the count but it will also consume the data

        // find the numbers greater then 3 but also in sorted order
        List<Integer> filteredNums = nums
                .stream().filter((n) -> n > 3).sorted().toList();
        System.out.println(filteredNums);

        // map the numbers to its multiplication by 2
        List<Integer> mappedNums = nums.stream().map(n -> n*2).toList();
        System.out.println(mappedNums);

        // even numbers
        List<Integer> even = nums.stream().filter(n -> n%2 ==0).toList();
        System.out.println(even);

        // upper case
        List<String> upper = names.stream().map(String::toUpperCase).toList();
        System.out.println(upper);

        // find the numbers greater then 5 but also in sorted order
        List<Integer> numsGreaterThenFive = nums
                .stream().filter((n) -> n > 5).toList();
        System.out.println(numsGreaterThenFive);

        // Comparator<Integer> comp = Comparator.reverseOrder(); this will give a Comparator which is reverse of natural order
//        Comparator<Integer> comp = Comparator.reverseOrder();
        // sort decending
        List<Integer> desc = nums.stream().sorted(Comparator.reverseOrder()).toList();
        System.out.println(desc);


        // ===================  Level 2 ====================

        // remove duplicates
        int [] a = new int[]{1,2,2,3,4,4,5,8,7,6};
        Stream<Integer> st = Arrays.stream(a).boxed();
        Stream<Integer> filtered = st.distinct();
        System.out.println(filtered.toList());

        // first number > 5
        System.out.println(Arrays.stream(a).filter(n -> n>5).findFirst().getAsInt());

        // any number divisible by 7
        System.out.println(nums.stream().anyMatch(n -> n > 7));

        // all numbers positive
        System.out.println(nums.stream().allMatch(n -> n>0));

        //count even numbers
        System.out.println(nums.stream().filter(n -> n % 2 ==0).count());


        // find a sum of all numbers
        System.out.println(nums.stream().mapToInt(n -> n).sum());

        // find avg of all numbers
        System.out.println(nums.stream().mapToInt(n -> n).average().getAsDouble());

        // find a max of all numbers
        System.out.println(nums.stream().mapToInt(n -> n).max().getAsInt());

        // joins
        System.out.println(names.stream().collect(Collectors.joining(",")));
        System.out.println(names.stream().collect(Collectors.joining("|")));

        // change to set
        System.out.println(names.stream().collect(Collectors.toSet()));




    }
}
