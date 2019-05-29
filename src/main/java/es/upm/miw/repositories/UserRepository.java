package es.upm.miw.repositories;

import es.upm.miw.documents.Role;
import es.upm.miw.documents.User;
import es.upm.miw.dtos.UserMinimumDto;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByMobile(String mobile);

    @Query(value = "{}", fields = "{ '_id' : 0, 'mobile' : 1, 'username' : 1}")
    List<UserMinimumDto> findAllUsers();

    @Query("{$and:["
            + "?#{ [0] == null ? { $where : 'true'} : { mobile : {$regex:[0], $options: 'i'} } },"
            + "?#{ [1] == null ? { $where : 'true'} : { username : {$regex:[1], $options: 'i'} } },"
            + "?#{ [2] == null ? { $where : 'true'} : { dni : {$regex:[2], $options: 'i'} } },"
            + "?#{ [3] == null ? { $where : 'true'} : { discount : {$regex:[3], $options: 'i'} } },"
            + "?#{ [4] == null ? { $where : 'true'} : { address : {$regex:[4], $options: 'i'} } },"
            + "{'roles':{$in:?5}}"
            + "] }")

    List<UserMinimumDto> findByMobileUsernameDniDiscountAddressLikeNullSafeandRoles(String mobile, String username, String dni, String discount, String address, Role[] roles);



}
