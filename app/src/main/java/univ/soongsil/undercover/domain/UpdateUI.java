package univ.soongsil.undercover.domain;

public interface UpdateUI<E> {
    void onSuccess(E result);
    void onFail();
}
