package util;

import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public enum Role {
		
	ADMINISTRATOR(Arrays.asList(
			RolePermission.READ_ALL_PRODUCTS
			,RolePermission.READ_ONE_PRODUCT
			,RolePermission.CREATE_ONE_PRODUCT
			,RolePermission.UPDATE_ONE_PRODUCT
			,RolePermission.DISABLE_ONE_PRODUCT
			
			,RolePermission.READ_ALL_CATEGORIES
			,RolePermission.READ_ONE_CATEGORY
			,RolePermission.CREATE_ONE_CATEGORY
			,RolePermission.UPDATE_ONE_CATEGORY
			,RolePermission.DISABLE_ONE_CATEGORY
			
			,RolePermission.READ_MY_PROFILE
			))
	
	, ASSISTANT_ADMINISTRATOR(Arrays.asList(
			RolePermission.READ_ALL_PRODUCTS
			,RolePermission.READ_ONE_PRODUCT
			,RolePermission.UPDATE_ONE_PRODUCT
			
			,RolePermission.READ_ALL_CATEGORIES
			,RolePermission.READ_ONE_CATEGORY
			,RolePermission.UPDATE_ONE_CATEGORY
			
			,RolePermission.READ_MY_PROFILE
			))
	
	, CUSTOMER(Arrays.asList(
			RolePermission.READ_MY_PROFILE
			));
	
	@Getter
	@Setter
	private List<RolePermission> permissions;
}
