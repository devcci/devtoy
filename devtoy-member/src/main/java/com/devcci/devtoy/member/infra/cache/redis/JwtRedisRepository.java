package com.devcci.devtoy.member.infra.cache.redis;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JwtRedisRepository extends CrudRepository<MemberRefreshToken, String> {
}
