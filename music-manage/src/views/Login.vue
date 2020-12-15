<template>
    <div class="login-wrap">
        <div class="ms-title">music 后台管理登录</div>
        <div class="ms-login">
            <el-form :model="adminFrom" :rules="rule" ref="adminFromRef" @keyup.enter.native="loginAdmin">
                <el-form-item prop="username">
                    <el-input v-model="adminFrom.username" placeholder="用户名"></el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input type="password" v-model="adminFrom.password" placeholder="密码"></el-input>
                </el-form-item>
                <div class="login-btn">
                    <el-button type="primary" @click="loginAdmin">登录</el-button>
                </div>
            </el-form>
        </div>
    </div>
</template>

<script>
    export default {
        data: function () {
            return {
                //表单对象
                adminFrom: {
                    username: "",
                    password: ""
                },
                //表单验证规则
                rule: {
                    username: [
                        {required: true, message: "请输入用户名", trigger: "blur"},
                        {min: 5, max: 12, message: '长度在 5 到 12 个字符', trigger: 'blur'}
                    ],
                    password: [
                        {required: true, message: "请输入密码", trigger: "blur"},
                        {min: 6, max: 12, message: '长度在 6 到 12 个字符', trigger: 'blur'}
                    ]
                }
            }
        },
        methods: {
            //登录提交
            loginAdmin() {
                this.$refs.adminFromRef.validate(valid => {
                    if (valid) {
                        this.$http.post("admin/login",this.adminFrom).then(res => {
                            if (res.data.result == "success"){
                                this.$notify({
                                    title: "成功",
                                    message: "登录成功！欢迎"+res.data.message.username,
                                    type: "success"
                                })
                                window.localStorage.setItem("JWT_TOKEN",res.data.message.JWT_TOKEN)
                                window.localStorage.setItem("username",res.data.message.username)
                                this.$router.push("/Home")
                            }else {
                                this.$notify({
                                    title: "错误",
                                    message: res.data.message,
                                    type: "error"
                                })
                            }
                        })
                    } else {
                        return;
                    }
                })
            }
        }
    }
</script>

<style scoped lang="less">
    .login-wrap {
        position: relative;
        background: url("../assets/img/background.jpg");
        background-attachment: fixed;
        background-position: center;
        background-size: cover;
        width: 100%;
        height: 100%;
    }

    .ms-title {
        position: absolute;
        top: 50%;
        width: 100%;
        margin-top: -230px;
        text-align: center;
        font-size: 30px;
        font-weight: 600;
        color: #fff;
    }

    .ms-login {
        position: absolute;
        left: 50%;
        top: 50%;
        width: 460px;
        height: 210px;
        padding: 30px;
        margin-top: -160px;
        margin-left: -230px;
        border-radius: 12px;
        background: #fff;
    }

    .login-btn {
        text-align: center;
    }

    .login-btn {
        button {
            width: 100%;
            height: 36px;
        }
    }
</style>
