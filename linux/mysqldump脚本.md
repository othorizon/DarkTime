# mysqldump脚本

``` bash
#!/bin/bash
#set -x

oldifs=$IFS
dump(){
mysqldump -h10.0.0.0 -uadmin -P9800 -p123456 --opt --single-transaction boss $1 | mysql  -uroot -P8306 -p123456 -C boss
}
dtd(){
        IFS=$oldifs
        arr=($1)
        for table in ${arr[@]}
        do
            echo start dump $table
            dump $table
        done
        IFS=$'\n'
}

IFS=$'\n'

#crm_measurement_units  crm_department crm_role_extend crm_price_item_ref_measure_time crm_time_units
#crm_special_rules_item crm_special_rules_product_item_rel crm_special_rules_product_type_rel
#dw_manual_rule dw_slice_rule
#crm_account_director crm_employee crm_region_base

tables="
crm_product_type crm_account crm_contract contract_account_release contract_product_price
crm_price_definition crm_price_detail
crm_special_rules_discount crm_contract_sprules crm_contract_sprules_regions
crm_rule_use_log crm_rule_use_lock crm_manual_rule crm_manual_rule_lock
month_bill_locked month_bill_summary_new
"

for table in ${tables[@]}
do
        read -r -p $table" ? [Y/n] " input
        case $input in
                [yY][eE][sS]|[yY]) dtd "${table}" ;;
                *) echo "No";;
        esac
done
```

`--opt`参数后面跟着的`--single-transaction`可以实现不锁住dump数据的表