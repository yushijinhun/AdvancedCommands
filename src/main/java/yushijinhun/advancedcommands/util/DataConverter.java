package yushijinhun.advancedcommands.util;

public interface DataConverter<K, V> {

	V convert(K src);

}
