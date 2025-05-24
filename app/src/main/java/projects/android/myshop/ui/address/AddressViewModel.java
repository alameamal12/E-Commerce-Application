package projects.android.myshop.ui.address;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import projects.android.myshop.data.repository.AddressRepository;
import projects.android.myshop.db.entity.AddressEntity;


// viewmodel for AddressViewModel
public class AddressViewModel extends ViewModel {
    private final AddressRepository addressRepository;

    public AddressViewModel(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    // insert or update address
    public Single<Long> upsert(@NonNull AddressEntity address) {
        return addressRepository.upsert(address);
    }

    // get address by addressId
    public Flowable<AddressEntity> getAddressById(long addressId) {
        return addressRepository.getAddressById(addressId);
    }

}
