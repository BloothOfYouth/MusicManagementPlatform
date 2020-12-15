<template>
    <div>
        <the-header></the-header>
        <the-aside></the-aside>
        <div class="content-box" :class="{'content-collapse':collapse}">
            <router-view></router-view>
        </div>
    </div>
</template>

<script>
    import bus from "../assets/js/bus";
    import TheAside from "../components/TheAside";
    import TheHeader from "../components/TheHeader";
    export default {
        name: "Home",
        components: {
            TheAside,
            TheHeader
        },
        data() {
            return {
                collapse: ''
            }
        },
        created() {
            bus.$on('collapseChange',msg => {
                this.collapse = msg
            })
        }
    }
</script>

<style scoped lang="less">

</style>
